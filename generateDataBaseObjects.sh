#   Copyright 2014-2015 Groupon, Inc
#   Copyright 2014-2015 The Billing Project, LLC
#
#   The Billing Project licenses this file to you under the Apache License, version 2.0
#   (the "License"); you may not use this file except in compliance with the
#   License.  You may obtain a copy of the License at:
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
#   WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
#   License for the specific language governing permissions and limitations
#   under the License.

JOOQ_VERSION=3.5.0
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -Dartifact=org.jooq:jooq:$JOOQ_VERSION -DrepoUrl=http://sonatype.org
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -Dartifact=org.jooq:jooq-meta:$JOOQ_VERSION -DrepoUrl=http://sonatype.org
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -Dartifact=org.jooq:jooq-codegen:$JOOQ_VERSION -DrepoUrl=http://sonatype.org

M2_REPOS=~/.m2/repository;
JOOQ="$M2_REPOS/org/jooq";
MYSQL="$M2_REPOS/mysql/mysql-connector-java/5.1.33/mysql-connector-java-5.1.33.jar";
JARS="$JOOQ/jooq/$JOOQ_VERSION/jooq-$JOOQ_VERSION.jar:$JOOQ/jooq-meta/$JOOQ_VERSION/jooq-meta-$JOOQ_VERSION.jar:$JOOQ/jooq-codegen/$JOOQ_VERSION/jooq-codegen-$JOOQ_VERSION.jar:$MYSQL:.";

java -cp $JARS org.jooq.util.GenerationTool src/main/resources/gen.xml
