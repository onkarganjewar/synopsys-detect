# Shell script configuration

The ${solution_name} Bash and PowerShell scripts can be configured by setting the following (optional) environment variables.

In Bash, an environment variable can be set as follows:

    export ENV_VAR_NAME=value

In PowerShell, an environment variable can be set as follows:

    $Env:ENV_VAR_NAME = value

## Download the ${solution_name} .jar from a given URL

*** Environment variable: DETECT_SOURCE ***

*** Value: The URL of the ${solution_name} .jar file to download and run. ***

## Run a given ${solution_name} version

*** Environment variable: DETECT_LATEST_RELEASE_VERSION ***

*** Value: A Detect version (example: 5.6.2) ***

If you would like to run a ${solution_name} version other than the latest, set DETECT_LATEST_RELEASE_VERSION to the ${solution_name} version you would like to run (for example: 5.6.2).
DETECT_SOURCE has precedence over DETECT_LATEST_RELEASE_VERSION.

You can see the available ${solution_name} versions in [Artifactory](${binary_repo_url_base}/webapp/#/artifacts/browse/tree/General/${binary_repo_repo}/${binary_repo_pkg_path}/${project_name}).

## Run the latest version of a given major version of ${solution_name}

*** Environment variable: DETECT_VERSION_KEY ***

*** Value: DETECT_LATEST (default), DETECT_LATEST_5, DETECT_LATEST_4 ***

If neither DETECT_SOURCE nor DETECT_LATEST_RELEASE_VERSION is specified, the script will use the version key to query Artifactory for the correct version to download.
By default it will look for DETECT_LATEST, however the detect artifactory also includes keys for (some of) the major versions of ${solution_name} such as DETECT_LATEST_4.

You can view the available values for DETECT_VERSION_KEY in [Artifactory](${binary_repo_url_base}/webapp/#/artifacts/browse/tree/Properties/${binary_repo_repo}/${binary_repo_pkg_path}/${project_name}).

## Use the given directory as the download location for the ${solution_name} .jar

*** Environment variable: DETECT_JAR_PATH ***

*** Value: The path to the .jar file download directory ***

If DETECT_JAR_PATH is provided, the script will use this location when downloading and running detect. The location of the jar will be DETECT_JAR_PATH/${project_name}-{version}.jar. The Bash script will default to '/tmp' if no option is specified.

## As a fallback, use the given directory as the download location for the ${solution_name} .jar (PowerShell only)

*** Environment variable: TMP ***

*** Value: The path to a directory for temporary files ***

If DETECT_JAR_PATH is not provided, the script will use the environment 'TMP' variable as the folder for the ${solution_name} .jar path.

## As a fallback, create a tmp dir in the given home directory and use it as the download location for the ${solution_name} .jar (PowerShell only)

*** Environment variable: $HOME ***

*** Value: The path to the user's home directory ***

If DETECT_JAR_PATH is not provided and no 'TMP' variable can be found, the '$HOME/tmp' folder will be used for the ${solution_name} jar path.

## Do not exit the shell on completion (PowerShell only)

*** Environment variable: DETECT_EXIT_CODE_PASSTHRU ***

*** Value: 1 ***

Setting this variable to '1' will cause the script to simply return the exit code but not exit.
By default, the ${solution_name} PowerShell script will exit with the exit code of ${solution_name}.
This is desirable because many CI's such as TFS will look at the scripts exit code to decide build status.
However it may be undesirable to exit the script in some situations, such as when debugging in a terminal.

## Skip the test for the presence of the java command (PowerShell only)

*** Environment variable: DETECT_SKIP_JAVA_TEST ***

*** Value: 1 ***

Setting this variable to '1' causes the script not to ensure that java is on the path. By default the script will attempt to execute "java -version" to ensure that java is available and executable.

## Add the given options to any curl commands executed (Bash only)

*** Environment variable: DETECT_CURL_OPTS ***

*** Value: curl command options (a string) ***

Use this variable to add options to the curl command used to download files such as the ${solution_name} .jar file.
For example, you can use this variable to set proxy settings for curl.
The PowerShell script does not support this as it does not use curl.
To supply proxy information to the PowerShell you can simply set the ${solution_name} proxy settings as environment variables.

## Add the given options to the java command (Bash only)

*** Environment variable: DETECT_JAVA_OPTS ***

*** Value: java command options (a string) ***

Use this variable to add options to the java command used to execute ${solution_name}.
The PowerShell script does not currently support this setting.