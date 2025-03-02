#!/usr/bin/env bash

<<COMMENT
This script is used to configure an AWS instance with the necessary software to run a Java application.
It also clones a repository from GitHub and runs the application.
The following environment variables must be set:
- TOKEN: GitHub token with access to the repository
- GITHUB_USER: GitHub user that owns the repository
- GITHUB_REPO: Name of the repository
COMMENT

handle_error() {
    echo "Error on line $1"
    exit 1
}

trap 'handle_error $LINENO' ERR

if [ -z "$TOKEN" ]; then
    echo "TOKEN is not set. Exiting..."
    exit 1
fi

if [ -z "$GITHUB_USER" ]; then
    echo "GITHUB_USER is not set. Exiting..."
    exit 1
fi

if [ -z "$GITHUB_REPO" ]; then
  echo "GITHUB_REPO is not set. Exiting..."
  exit 1
fi

printf "Starting AWS instance configuration\n"

sudo dd if=/dev/zero of=/swapfile count=2048 bs=1MiB
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
sudo cp /etc/fstab /etc/fstab.bak
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

sudo apt update && sudo apt -y install zip unzip nmap apache2 certbot eza

curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 21.0.3-tem

sdk use java 21.0.3-tem
export JAVA_HOME="$HOME/.sdkman/candidates/java/current"
export PATH="$JAVA_HOME/bin:$PATH"

sudo service apache2 start

sudo curl -H "Authorization: token $TOKEN" -o /etc/apache2/sites-available/proxyreverso.conf \
     -L "https://raw.githubusercontent.com/$GITHUB_USER/icc-352/main/practica-4/config/proxyreverso.conf"

sudo curl -H "Authorization: token $TOKEN" -o /etc/apache2/sites-available/seguro.conf \
     -L "https://raw.githubusercontent.com/$GITHUB_USER/icc-352/main/practica-4/config/seguro.conf"

cd $HOME
git clone https://$TOKEN@github.com/$GITHUB_USER/$GITHUB_REPO
cd $GITHUB_REPO

./gradlew shadowjar

java -jar $HOME/$GITHUB_REPO/app/build/libs/app.jar > $HOME/$GITHUB_REPO/app/build/libs/output.log 2> $HOME/$GITHUB_REPO/app/build/libs/error.log &

cd $HOME
git clone https://github.com/$GITHUB_USER/$GITHUB_SEGCOND_REPO
cd $GITHUB_SEGCOND_REPO

./gradlew shadowjar

java -jar $HOME/$GITHUB_SEGCOND_REPO/app/build/libs/app.jar > $HOME/$GITHUB_SEGCOND_REPO/app/build/libs/output.log 2> $HOME/$GITHUB_SEGCOND_REPO/app/build/libs/error.log &
