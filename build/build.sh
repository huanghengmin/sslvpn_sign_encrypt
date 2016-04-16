#!/bin/sh

##############################################################################
#
# Required environment settings:
#
# JAVA_HOME		- the root installation directory for Java
#
# Optional environment settings:
#
# OS			- the operating system name
# JDK			- the version of the JDK to use
#				  valid values: 1.7 (default)
# BASE			- the root of the build tree
#				  valid values: any directory, . (default)
# BUILD_TYPE	- the type of build you with to perform
#				  valid values: release, debug (default)
#
##############################################################################

## usage:
#	build [-debug] [<target>]

DEBUG_FLAG=""

if [ "$1" = "-debug" ] ; then
  DEBUG_FLAG="-debug"
  shift
fi

# Get the target of the build from the command line (default to all)
#TARGET=${1:-all}
TARGET="all"
if [ "$1" != "" ] ; then
    TARGET=$1
    shift
fi    

OTHER_ARGS=""
SPACE=" "
while [ $# -ne 0 ]
do 
	OTHER_ARGS=${OTHER_ARGS}${SPACE}$1
	shift
done

echo $OTHER_ARGS

# Get environment variables if they are set
OS=${OS:-`uname`}
JDK=${JDK:-1.7}
HERE=`pwd`
BASE=${BASE:-${HERE}/..}
BUILD_TYPE=${BUILD_TYPE:-debug}

# Prepare derived build variables
BUILDROOT=$BASE
BUILDDEST=$BASE/temp
BUILDFILE=$HERE/build.xml
BUILDTOOLS=d:/fartec/ichange/tools
BUILDTP=d:/fartec/ichange/sharelib


# Prepare OS specific variables
case $OS in
    "Windows_NT") 
        PATHSEP=${PATHSEP:-";"}
        JAVA_HOME=${JAVA_HOME:-c:/programs/jdk1.3}
		OS_TYPE=nt
        OS_NAME=nt
        OS_DIRNAME=winnt
        OS_SHAREDLIB_SUFFIX=dll
		OS_STATICLIB_SUFFIX=lib
        OS_EXECUTABLE_SUFFIX=.exe
        OS_SCRIPT_SUFFIX=.bat
    ;;
    "Linux")
        PATHSEP=${PATHSEP:-":"}
        JAVA_HOME=${JAVA_HOME:-/usr/local/bin/java}
		OS_TYPE=unix
        OS_NAME=linux
        OS_DIRNAME=linux
        OS_SHAREDLIB_SUFFIX=so
		OS_STATICLIB_SUFFIX=a
        OS_EXECUTABLE_SUFFIX=
        OS_SCRIPT_SUFFIX=
    ;;

    "AIX")
        PATHSEP=${PATHSEP:-":"}
        JAVA_HOME=${JAVA_HOME:-/usr/java140}
		OS_TYPE=unix
        OS_NAME=aix
        OS_DIRNAME=aix
        OS_SHAREDLIB_SUFFIX=so
		OS_STATICLIB_SUFFIX=a
        OS_EXECUTABLE_SUFFIX=
        OS_SCRIPT_SUFFIX=
    ;;

    "HP-UX")
        PATHSEP=${PATHSEP:-":"}
        JAVA_HOME=${JAVA_HOME:-/opt/java1.4}
		OS_TYPE=unix
        OS_NAME=hpux
        OS_DIRNAME=hpux
        OS_SHAREDLIB_SUFFIX=sl
		OS_STATICLIB_SUFFIX=a
        OS_EXECUTABLE_SUFFIX=
        OS_SCRIPT_SUFFIX=
    ;;
	
    *)
        echo This operating system not supported by build.sh
        exit 1
    ;;
esac

MAKE_BUILDTREE=1
export BUILD_TYPE
export MAKE_BUILDTREE

#set java home and export path
JAVA_HOME=${BUILDTP}/sun/jdk
export JAVA_HOME


# Set and export PATH
PATH=${JAVA_HOME}/bin${PATHSEP}${JAVA_HOME}/lib${PATHSEP}${PATH}
export PATH


# Set and export CLASSPATH
CLASSPATH=${CLASSPATH}${PATHSEP}${JAVA_HOME}/lib/tools.jar
CLASSPATH=${CLASSPATH}${PATHSEP}${BUILDTOOLS}/ant/ant.jar
CLASSPATH=${CLASSPATH}${PATHSEP}${BUILDTOOLS}/ant/optional.jar
CLASSPATH=${CLASSPATH}${PATHSEP}${BUILDTOOLS}/junit/junit.jar
CLASSPATH=${CLASSPATH}${PATHSEP}${BUILDTP}/apache/xalan/xalan.jar
CLASSPATH=${CLASSPATH}${PATHSEP}${BUILDTP}/apache/xerces/xml-apis.jar
CLASSPATH=${CLASSPATH}${PATHSEP}${BUILDTP}/apache/xerces/xercesImpl.jar
CLASSPATH=${CLASSPATH}${PATHSEP}${BUILDTP}/jakarta/oro/jakarta-oro.jar

export CLASSPATH

# build text list file


# Perform the build
$JAVA_HOME/bin/java -classpath $CLASSPATH -Djdk="$JDK" -Dbuild.target=$TARGET -Dbuild.dest=$BUILDDEST -Dbuild.files=$BASE -Dbuild.type.$BUILD_TYPE="true" -Dbuild.type=$BUILD_TYPE -Dos.$OS_TYPE="true" -Dos.$OS_NAME="true" -Dos.type=$OS_NAME -Dos.dirname=$OS_DIRNAME -Dos.sharedlib.suffix=$OS_SHAREDLIB_SUFFIX -Dos.staticlib.suffix=$OS_STATICLIB_SUFFIX -Dos.exe.suffix=$OS_EXECUTABLE_SUFFIX -Dos.script.suffix=$OS_SCRIPT_SUFFIX -Dmy.user=${USERNAME} -Dmy.host=${HOST} org.apache.tools.ant.Main  $DEBUG_FLAG -buildfile $BUILDFILE $TARGET $OTHER_ARGS