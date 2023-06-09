# FlashFrame

FlashFrame is a scaffold creating an AWS based single page web app.

# Stack

## Frontend

### Programming Language

Typescript

### Programming Framework

Angular

### UI

Angular Material + TailwindCSS

## Backend

### Programming Language

Kotlin

### Programming Framework

Spring MVC + Spring Boot

### Service

API Gateway + Lambda

### CDN

Cloudfront

### DNS

Route53

### Certificate

Certificate Manager

### User Management

Amazon Cognito

### Email Service

Amazon Simple Email Service

### File Storage

S3

## API Generation
OpenAPI + related tools

## Resource Management

CDK

## CI/CD

None


# Usage

There are only two manual operations now, modifying name servers to Route53 and requesting Prod permission for SES.

## Checkout

## Setup Environment Variable
You need to set up `AWS_ACCOUNT_ID` and `AWS_REGION` for building CDK

## Build
run `gradle build`

## Bootstrap

run

`cd CDK`

`cdk bootstrap`

## Deploy Route53

run `cdk deploy ${AppName}-Route53Stack` 

## Change Name Server of Your Domain to Route53
Login to your domain provider and change the name service to Route53 

## Deploy all

run `cdk deploy --all`

## TODO
- [ ] Add throttling to prevent accident cost in AWS
- [ ] Add payment support
- [ ] Add more Frontend pages
- [ ] Generate icon for FlashFrame
