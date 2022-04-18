package com.bigcorp.demo.controller;

import java.io.IOException;

import org.kohsuke.github.GHBranchProtectionBuilder;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bigcorp.demo.model.GHSimpleRepo;
import com.bigcorp.demo.model.GHSimpleRepoEvent;
import com.bigcorp.demo.model.GHSimpleUser.AccountType;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RepositoryControler {

    /**
     * Called when a repository is either created, imported or forked
     */
    @PostMapping(path = "/repolistener", produces = MediaType.APPLICATION_JSON_VALUE)
    public String onNew(final @RequestBody GHSimpleRepoEvent repoEvent) throws IOException {

        log.debug("Repository listener called ...");

        final GHSimpleRepo simpleRepo = repoEvent.getRepository();
        if (simpleRepo.getOwner().getType() == AccountType.ORGANIZATION) {

            final String repoEventAction = repoEvent.getAction(); // Provided during repository creation
            final String repoFullName = simpleRepo.getFullName();

            log.info("Event action : '{}' / Repository : '{}'", repoEventAction, repoFullName);

            if ("created".equals(repoEventAction)) {

                log.info("Must check for custom (required & expected) security configurations per repository");

                final GitHub github = GitHubBuilder.fromEnvironment().build();
                final GHOrganization ghOrganization = github.getOrganization(repoEvent.getOrganization().getLogin());

                final GHRepository ghRepository = ghOrganization.getRepository(repoEvent.getRepository().getName());

                final String defaultBranch = ghRepository.getDefaultBranch();
                
                if (defaultBranch != null) {
                    
                    log.info("Apply default branch protection rules on [{}]", defaultBranch);
                    
                    if (!ghRepository.getBranches().keySet().contains(defaultBranch)) {
                        log.warn("Default branch [{}] does not exist yet into the repository [{}], proceeding to its creation !", defaultBranch, repoFullName);
                        
                        final String sha1 = ghRepository.createContent()
                                .content("Default content ...")
                                .path("README.md")
                                .message("default commit message")
                                .commit()
                                .getCommit()
                                .getSHA1();
                        ghRepository.createRef("refs/heads/" + defaultBranch, sha1);
                        
                        log.info("Default branch [{}] created successfuly !", defaultBranch);
                    }

                } else {
                    // Write the code for the creation of the default branch here if you want to automate / secure the process ...
                    log.warn("For the sake of simplicity in this demonstration, no specific code will be written for initializing the default branch (nothing complicate though) ... "
                            + "Otherwise, just ask for it, anything is possible !");
                    log.warn("A specific policy could/should be applied in case a repository without default branch (or without branch at all) is newly created or imported");
                }
                

                // Must verify that default branch exists (for safety)

                // We assume default branch already exist

                final GHBranchProtectionBuilder ghBranchProtectionBuilder = ghRepository.getBranch(defaultBranch).enableProtection();
                ghBranchProtectionBuilder
                        // .addRequiredChecks("build")
                        // .restrictPushAccess()
                        // .requireBranchIsUpToDate()
                        .requireReviews()
                        .includeAdmins()
                        .enable();

                log.info("Create issue within the repository that outlines the protections that were needed");
                
                final String notifiedUser = github.getMyself().getLogin(); // User to notify via @mention mechanism
                
                ghRepository
                        .createIssue("Branch protection rules applied on default branch : " + defaultBranch)
                        .label("security")
                        .body("@" + notifiedUser)
                        .create();

                log.info("Terminated ...");

            } else {
                // Nothing to do !
            }

        } else {
            log.warn("This (demo) application is implemented only for Github organizations, not users or other account types : {}", simpleRepo.getOwner().getType());
        }

        return "{ 'status' : 'done' }";
    }

}
