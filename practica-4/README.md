# AWS Instance Initialization Script

## Overview

This script is designed to set up an AWS instance with necessary configurations and software installations. It includes steps to enable swap memory, install essential software packages, configure Apache, and deploy a Java application. The script also ensures secure handling of configuration files and logs output and error information for troubleshooting.

## Prerequisites

- An AWS instance with Ubuntu or a similar Linux distribution.
- A GitHub personal access token with access to the required repositories.
- `sdkman` for managing Java installations.
- Apache server for hosting web applications.

## Usage

1. **Set the GitHub Token**:
   Ensure the `TOKEN` environment variable is set with your GitHub personal access token:
   ```sh
   export TOKEN="YOUR_ACCESS_TOKEN"
   ```

2. **Run the Script**:
   Execute the script on your AWS instance:
   ```sh
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
   - Clones the ORM project repository.
   - Builds the project using `gradlew`.
   - Runs the Java application and redirects the output and error logs to appropriate files.

## Error Handling

The script includes error handling to ensure that any issues encountered during execution are reported, and the script exits gracefully.

## Logs

Application logs and error logs are stored in:
- Output Log: `$HOME/practica-3/app/build/libs/output.log`
- Error Log: `$HOME/practica-3/app/build/libs/error.log`

Ensure to check these files for any runtime issues or errors.

## Security Considerations

- The GitHub token is handled securely by using an environment variable.
- Ensure your token is stored securely and not hard-coded in the script.

