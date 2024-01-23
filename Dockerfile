FROM quay.io/wildfly/wildfly

ADD build/libs/SpringOAuth2Test.war /wildfly/standalone/deployments/
