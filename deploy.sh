#!/usr/bin/env bash
if [[ -f ".env" ]]; then
 echo "file exists"
 export $(cat .env | xargs)
fi
curl --max-time 15 -T "{build/libs/PhpClean.jar,build/libs/PhpClean-nightly.xml}" $DEPLOY_URI