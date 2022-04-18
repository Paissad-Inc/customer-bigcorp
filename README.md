## Scenario

Let’s take a look at the customer scenario: "Our security team is asking for help ensuring proper reviews are being done to code being added into our repositories. We have hundreds of repositories in our organization. What is the best way we can achieve at scale? We are new to some of the out-of-the-box settings and the GitHub API. Can you please help us create a solution that will accomplish this for our security team?"

### How to retrieve and run the demo application
Either download the last available .jar and execute it directly, or build the project from source in order the to have the latest from source code

#### How to build from source code (not necessary if .jar already retrieved)
java >= 11
maven >= 3.8.5
> mvn -U clean package

Retrieve the .jar file from the <PROJECT_DIR>/target  directory

#### How to execute
The environment variable GITHUB_OAUTH must be set. Follow the following link for the [procedure of token creation](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)
The created token ***must*** have the ***repo*** scopes.

From a command line interface (shell or cmd) :
> export GITHUB_OAUTH=<my_token>
> java -jar gh-bigcorp-demo-0.0.1-SNAPSHOT.jar 

The application will listen on port 8080 by default. If you want to change the default port, you can start the application the following way:
> java -jar gh-bigcorp-demo-0.0.1-SNAPSHOT.jar --server.port=OTHER_PORT

Verify the application is started with the following example of log
> 022-04-18 17:30:09 - INFO  - Started GhBigcorpDemoApplication in 3.302 seconds (JVM running for 4.551)

and/or by verifying your port is in listen mode
> nc -v localhost 8080

### Find your public IP address or public DNS 
That public IP  or DNS is necessary and must be accessible from github.com 
Examples :
> 88.55.44.23 
> my-pub-adress.org

### Configuration of the webhook for the repository creations

Now that the application is started, we must create an [organization webhook](https://docs.github.com/en/rest/reference/orgs#webhooks) which will send repository events to our freshly started application.
or reach the following address by replacing YOUR_ORG https://github.com/organizations/<YOUR_ORG>/settings/hooks/new

Webhook setttings:
> payload url : ***http://<ip_address>:8080/repolistener***
> content-type : ***application/json***
> Select individual events : ***Repositories***

Whenever you create a new repository, you can verify the log from the application
> INFO  - Event action : 'created' / Repository : 'Paissad-Inc/bb'
> INFO  - Must check for custom (required & expected) security configurations per repository
> INFO - Apply default branch protection rules on [develop]
> INFO  - Terminated ...

You can verify the branch protection rules settings from https://github.com/<YOUR_ORG>/<YOUR_REPO>/settings/branches
Cf docs [related to branch protection rules](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/defining-the-mergeability-of-pull-requests/managing-a-branch-protection-rule) 

