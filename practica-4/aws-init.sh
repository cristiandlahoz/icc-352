#!/usr/bin/env bash
printf "Iniciando la configuración de la instancia de AWS\n"

# Habilitando la memoria de intercambio.
sudo dd if=/dev/zero of=/swapfile count=2048 bs=1MiB
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
sudo cp /etc/fstab /etc/fstab.bak
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

# Instando los software necesarios para probar el concepto.
sudo apt update && sudo apt -y install zip unzip nmap apache2 certbot eza

# Instalando la versión sdkman y java
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Utilizando la versión de java 17 como base.
sdk install java 21.0.3-tem

# Subiendo el servicio de Apache.
sudo service apache2 start

curl https://raw.githubusercontent.com/cristiandelahooz/icc-352/refs/heads/main/practica-4/config/proxyreverso.conf?token=GHSAT0AAAAAAC6ZYOAHCD2JVEK4SL7VMNJQZ6DNBSA -o /etc/apache2/sites-available/proxyreverso.conf

# Creando las estructuras de los archivos.
sudo mkdir -p /var/www/html/app1 /var/www/html/app2

# Creando los archivos por defecto.
printf "<h1>Sitio Aplicacion #1</h1>" | sudo tee /var/www/html/app1/index.html
printf "<h1>Sitio Aplicacion #2</h1>" | sudo tee /var/www/html/app2/index.html

# Clonando el proyecto ORM y moviendo a la carpeta descargada.
cd ~/
git clone https://github.com/cristiandelahooz/practica-3
cd practica-3

# Ejecutando la creación de fatjar
./gradlew shadowjar

# Subiendo la aplicación puerto por defecto.
java -jar ~/practica-3/build/libs/app.jar > ~/practica-3/build/libs/salida.txt 2> ~/practica-3/build/libs/error.txt &
