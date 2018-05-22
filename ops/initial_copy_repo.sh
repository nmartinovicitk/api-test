#!/bin/bash

#Empty repo must exist on the github account!
echo "**************************************************************************"
echo "Enter name of the repo that you created on the github for the new project."
read -p 'Enter repository name: ' repo_name

#Delete .git and initialize new git to get rid of the previous commits
cd ..
rm -rf .git
git init

echo "*****************************"
echo "Adding remote for new project"
echo "*****************************"
git remote add origin https://github.com/nmartinovicitk/"$repo_name".git

#ovde da ide sed koji menja README ($repo_name)
#ovde ide sed koji menja u Proc fajlu ime aplikacije ($repo_name)

git add *
git add .gitignore

echo "***********************************************"
echo "Commit starter as initial commit to new project"
echo "***********************************************"
git commit -m "Initial commit"

echo "************************"
echo "Push starter to new repo"
echo "************************"

#This will push the code from the starter to the remote of the new repo
#thus effectively copying the starter to new project without previous commits
if git push -u origin master; then
   cd ..
   mv api-starter $repo_name
   cd $repo_name
   echo "***********************************************"
   echo "SUCCESS! Repository for the new project set up!"
   echo "You can continue from within this dir!"
   echo "***********************************************"
else
   echo "**************************************"
   echo "ERROR! ERROR! ERROR!"
   echo "Oh no! Something went wrong!!!"
   echo "**************************************"
fi

