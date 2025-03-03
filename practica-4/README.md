# AWS Instance Initialization Script

## Overview

This script is designed to set up an AWS instance with necessary configurations and software installations. It includes steps to enable swap memory, install essential software packages, configure Apache, and deploy a Java application. The script also ensures secure handling of configuration files and logs output and error information for troubleshooting.

## Prerequisites

- An AWS instance with Ubuntu or a similar Linux distribution.
- A GitHub personal access token with access to the required repositories.
- `sdkman` for managing Java installations.
- Apache server for hosting web applications.

## Usage

1. **Set the Environment Variables**:
   Ensure the following environment variables are set with your GitHub personal access token, GitHub username, and repository name:
   ```sh
   export TOKEN="YOUR_ACCESS_TOKEN"
   export GITHUB_USER="YOUR_GITHUB_USERNAME"
   export REPO_NAME="YOUR_REPOSITORY_NAME"
   ```

2. **Download and Run the Script**:
   Execute the following command on your AWS EC2 instance to download the initialization script:
   ```sh
   curl -H "Authorization: token $TOKEN" -o aws-init.sh -L "https://raw.githubusercontent.com/$GITHUB_USER/icc-352/main/practica-4/aws-init.sh"
   ```
   After setting the environment variables, run the script:
   ```sh
   chmod +x aws-init.sh
   ./aws-init.sh
   ```

## Script Details

1. **Swap Memory Configuration**:
   - Enables swap memory to improve instance performance.

2. **Software Installation**:
   - Installs essential packages such as `zip`, `unzip`, `nmap`, `apache2`, `certbot`, and `eza`.

3. **Java Installation**:
   - Installs `sdkman` and uses it to install Java 17.

4. **Apache Configuration**:
   - Starts the Apache service.
   - Downloads and applies the Apache configuration file securely using the GitHub API and the provided token.

5. **File Structure Setup**:
   - Creates directories for hosting two web applications.
   - Creates default HTML files for both applications.

6. **Java Application Deployment**:
   - Clones the ORM project repository using the GitHub token.
   - Builds the project using `gradlew`.
   - Runs the Java application and redirects the output and error logs to appropriate files.

## Error Handling

The script includes error handling to ensure that any issues encountered during execution are reported, and the script exits gracefully.

## Logs

Application logs and error logs are stored in:
- Output Log: `$HOME/$REPO_NAME/app/build/libs/output.log`
- Error Log: `$HOME/$REPO_NAME/app/build/libs/error.log`

Ensure to check these files for any runtime issues or errors.

# Instructions to Obtain a Certificate with Certbot

Follow these steps to obtain an SSL certificate using Certbot with the manual DNS method.

## Instructions

1. Cerbot is already installed due to the prev script.

2. Run the following command in your terminal, replacing `your-email@domain.com` with your email address and `yourdomain.com` with your domain:

    ```bash
    sudo certbot certonly --manual --preferred-challenges dns \
      --email your-email@domain.com --agree-tos --no-eff-email \
      -d "*.yourdomain.com" -d "yourdomain.com"
    ```

3. After running the command, Certbot will prompt you to create a DNS TXT record for each specified domain. You need to access your domain provider's DNS settings and add the TXT records that Certbot specifies.

4. Once you have added the TXT records, return to the terminal and press Enter so that Certbot can verify the records.

5. If everything is correct, Certbot will generate and save the certificates on your server. The certificates are typically saved in the `/etc/letsencrypt/live/yourdomain.com/` directory.

6. Configure your web server (e.g., Apache or Nginx) to use the generated certificates.

## Certificate Renewal

Remember that Let's Encrypt certificates are valid for 90 days. You will need to renew them before they expire. You can set up a cron job for automatic renewal or manually run the following command:

```bash
sudo certbot renew
```

That's it! You should now have a valid SSL certificate for your domain.
## Security Considerations

- The GitHub token is handled securely by using an environment variable.
- Ensure your token is stored securely and not hard-coded in the script.

[![Watch the video](https://img.youtube.com/vi/A2YatL-raWg/maxresdefault.jpg)](https://www.youtube.com/watch?v=A2YatL-raWg)
