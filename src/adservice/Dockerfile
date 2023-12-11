# Copyright 2020 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

FROM eclipse-temurin:21@sha256:9d007c093e624bc410f113ee09f5209d3263e0bada7524e4de99eac9fbef06a5 as builder

WORKDIR /app

COPY ["build.gradle", "gradlew", "./"]
COPY gradle gradle
RUN chmod +x gradlew
RUN ./gradlew downloadRepos

COPY . .
RUN chmod +x gradlew
RUN ./gradlew installDist

RUN chmod a+x /app/build/install/hipstershop/lib/hipstershop-0.1.0-SNAPSHOT.jar
#RUN chmod a+x /app/build/install/hipstershop/bin/AdService

#FROM eclipse-temurin:21-jre-alpine@sha256:b5a1250bb72d804d49471207e2ebb574caffc13ac76f99618fb5ea06af621056
FROM cgr.dev/chainguard/jre

#RUN apk add --no-cache ca-certificates

#ENV DISABLE_PROFILER=1

WORKDIR /home
COPY --from=builder /app/build/install/hipstershop /home/.

EXPOSE 9555
#ENTRYPOINT ["/app/build/install/hipstershop/bin/AdService"]
ENTRYPOINT ["java", "-jar", "/home/lib/hipstershop-0.1.0-SNAPSHOT.jar"]
