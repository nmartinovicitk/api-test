#!/bin/bash

cd ..
#Empty repo must exist on the github account!
echo "**************************************************************************"
echo "Enter root of the name for the heroku server."
read -p 'Enter heroku app name: ' heroku_project

read -p 'Enter server type (dev/staging/production): ' server_type


heroku login

heroku apps:create "$heroku_project"-"$server_type"

heroku addons:create heroku-postgresql:hobby-dev --app "$heroku_project"-"$server_type"

DB_USER=$(heroku config:get DATABASE_URL --app "$heroku_project"-"$server_type" | grep -oP  '(?<=postgres://).*' | cut -d@ -f1 | cut -d: -f1)
DB_PASS=$(heroku config:get DATABASE_URL --app "$heroku_project"-"$server_type" | grep -oP  '(?<=postgres://).*' | cut -d@ -f1 | cut -d: -f2)

heroku config:set DATABASE_USERNAME=$DB_USER --app "$heroku_project"-"$server_type"
heroku config:set DATABASE_PASSWORD=$DB_PASS --app "$heroku_project"-"$server_type"

heroku addons:create papertrail:choklad --app "$heroku_project"-"$server_type"

psql `heroku config:get DATABASE_URL -a api-test-dev` < conf/evolutions/default/*.sql


#heroku ps:scale web=1 --app "$heroku_project"-"$server_type"

# For a proc file
# web target/universal/stage/bin/api-starter -Dhttp.port=$PORT 

#Add database backups
#heroku addons:add pgbackups:auto-month --app "$heroku_project"-"$server_type"
#heroku config:get DATABASE_URL | grep -oP  '(?<=postgres://).*' | cut -d@ -f1 | cut -d: -f2
