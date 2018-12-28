#!/usr/bin/env bash
if [[ -f ".env" ]]; then
 export $(cat .env | xargs)
fi
export ORG_GRADLE_PROJECT_version=$(date +%Y.%-m.%-d%H%M%S)
./gradlew check buildPlugin patchRepositoryXml && \
curl -s --max-time 15  -F 'file[]=@build/libs/PhpClean.jar' -F 'file[]=@build/libs/PhpClean-nightly.xml' $DEPLOY_URI