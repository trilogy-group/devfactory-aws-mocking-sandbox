# Sandbox for AWS call simulation

Based on: Dynamic REST API simulation and for application development & QA testing
   - Visit: https://www.smockin.com
   - Contact: info@smockin.com
   - Forked from: https://github.com/mgtechsoftware/smockin

<br />


## OVERVIEW

sMockin-sandbox is an AWS mock enhancement of sMockin - an API mocking tool used to dynamically simulate HTTP endpoints.

Featuring a rich UI and an inbuilt HTTP mock server, mocking your existing or new API model can be done quickly and without any coding or scripting.

sMockin-sandbox is a container and contains small web server

<br/>

### KEY FEATURES

* Proxies undefined requests further, including AWS ones
* Accepts AWS requests at /aws-service-[service], for example:
 - /aws-service-ce
 - /aws-service-sts
 - /aws-service-ec2
 - ...
* Create dynamic API mocks to mimic real world application behaviour.
* Run sMockin centrally and create user accounts for your team.
* Monitor and log traffic going to the HTTP mock server.
* A complete UI solution requiring zero coding.


<br/>

### REQUIREMENTS

   - Docker
   - set endpoint for AWS calls to http://[docker-ip]:8000/aws-service-[service]
   - initial smockin mocks in smockin-to-import.zip (exported previously from running instance). In case file is not provided, mock database is empty

<br />

## QUICK START 

#### Run sMockin-sandbox as just a docker container with proxy ready-to-use

Running successfully sMockin-sandbox requires:
- map external volume to "/app/sandbox-data/" (optional but recommended: with initial set of mocks - json files)
- tunnel ports 8000 and 8001 outside docker to have access to mock server (port 8000) and managing application (port 8001)
- specified (as environment variables) AWS credentials, i.e.:
    * AWS_ACCESS_KEY_ID
    * AWS_SECRET_ACCESS_KEY
    * AWS_DEFAULT_REGION

Hence, running docker can be like:

*docker run --rm -dt -p 8000:8000 -p 8001:8001 -e AWS_ACCESS_KEY_ID=[your-aws-key] -e AWS_SECRET_ACCESS_KEY=[your-aws-secret] -e AWS_DEFAULT_REGION=us-east-1 -v /tmp/data-for-smocking-sandbox:/app/sandbox-data/ registry.devfactory.com/devfactory/smockin-sandbox /bin/bash -c "/app/start-app.sh"*

#### Docker

Docker image is created automatically during pipeline, but in case you want to do it by your own, pls execute it inside **docker** folder:

*docker build -t registry.devfactory.com/devfactory/smockin-sandbox .*

and then publish (if it's a new version which should be used by everyone)

*docker push registry.devfactory.com/devfactory/smockin-sandbox*


#### Using sMockin-sandbox in your project

You can use sMocking-sandbox in your project as a standalone, you can even monitor results, just run in interactive mode:

*docker run --rm -it -p 8000:8000 -p 8001:8001 -e AWS_ACCESS_KEY_ID=[your-aws-key] -e AWS_SECRET_ACCESS_KEY=[your-aws-secret] -e AWS_DEFAULT_REGION=us-east-1 -v /tmp/data-for-smocking-sandbox:/app/sandbox-data/ registry.devfactory.com/devfactory/smockin-sandbox /bin/bash"*

and once started, run 

*/app/start-app.sh*x    

#### Import changed data
When run start-app.sh json files (monted in /app/sandbox-data) are automatically imported once the server is started.
In case you update them and you want to update smockin, you can do either: 
* restart start-app.sh (easiest option :), or
* execute scirpts: 
    * /app/create_import_archive.sh
    * /app/import_smockin.sh 
    
#### Start from scratch (new project)

To start using new project with the sandbox you need:
- run docker container as described above on a machine with either public IP available (and opened ports) 
or machine with the same (or paired) VPC as environment you want to test,
- start docker (as described above). Without any input json files it fallbacks to dummy structure
with 404 defined - just to accelerate process.

Default view:

<p align="center">
  <img src="/public/image/sandbox/main-emtpy-view.png" width=400 />
</p>

Default rule:

<p align="center">
  <img src="/public/image/sandbox/default-rule.png" width=400 />
</p>

#### Export prepared mock

You should store text files in Github, to easily follow changes, in this case, in *json* files.
To export such json please use dedicated Export button, in an endpoint you want to export, like below:

<p align="center">
  <img src="/public/image/sandbox/export-button.png" width=400 />
</p>
 