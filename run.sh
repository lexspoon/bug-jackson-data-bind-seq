set -e

#JVER=2.2.0
#JVER=2.6.0
JVER=2.6.4

CP=jackson-annotations-$JVER.jar:jackson-core-$JVER.jar:jackson-databind-$JVER.jar

rm -rf bin
mkdir bin
javac -d bin -cp $CP $(find src -name '*.java')
java -cp bin:$CP com.semmle.bcache.Bug


