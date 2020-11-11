#!/bin/bash

# Check Java 8 is installed
java -version 2>&1 | grep -i version | awk -F '"' '/version/ {print $2}' > ./java.version
IFS='.' read major minor extra < ./java.version

if (( major == 1 )) ; then
    SMOCKIN_JAVA_VERSION=$minor
else
    SMOCKIN_JAVA_VERSION=$major
fi

if (( SMOCKIN_JAVA_VERSION < 8 ))
then
  echo ""
  echo "Smockin requires Java 8 or later to run"
  echo ""
  echo "Please visit 'http://www.java.com/en/download' to install the latest Java Runtime Environment (JRE)"
  echo ""
  echo "If you have installed Java and are still seeing this message, then please ensure this is present in your PATH"
  echo ""

  exit
fi


echo ""
echo "Remove all SMOCKIN related DB and configuration files from this machine? (Y/N)"
echo ""

read USER_DECISION

if [ "${USER_DECISION}" != "Y" ]
then
    exit 0
fi



APP_DIR_PATH="${HOME}/.smockin"


#
# DELETE THE '.smockin' DIRECTORY
#
if [ ! -d "${APP_DIR_PATH}" ]
then

  echo ""
  echo "Smockin is not installed..."
  echo ""

  exit

else

  # Shutdown default H2 DB if running...
  ./shutdown.sh

  echo ""
	echo "Removing $APP_DIR_PATH in user home..."
  echo ""

  rm -rf $APP_DIR_PATH

  echo ""
  echo ""
  echo "*** Smockin has been uninstalled ***"
  echo ""
  echo ""

fi
