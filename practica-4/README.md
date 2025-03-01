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

## Security Considerations

- The GitHub token is handled securely by using an environment variable.
- Ensure your token is stored securely and not hard-coded in the script.

