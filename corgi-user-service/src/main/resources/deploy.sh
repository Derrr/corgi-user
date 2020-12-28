#/bin/bash

set -x

TAG=$1

DATE=`date +%s`
WORK_DIR="/root/data/corgi-user"
GIT_DIR=$WORK_DIR"/corgi-user/corgi-user-service"
TAG_DIR=$WORK_DIR"/code"
SOURCE_DIR=$WORK_DIR"/source"
PACKAGE_NAME="corgi-user.jar"
PID=$(ps aux | grep " ${PACKAGE_NAME}$" | grep -v grep | awk '{print $2}' )
echo $PID
JAVA_OPTS="-server -Xms1024m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m -XX:-UseGCOverheadLimit -Xverify:none -Xnoclassgc -XX:+DisableExplicitGC -XX:+PrintGCDetails -Xloggc:/root/data/corgi-user/logs/gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps  -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8 -Djava.awt.headless=true"
#JAVA_OPTS="-Dspring.config.location=/data/shoe-inspire-api/inspire-api/config/app.properties"
#JAVA_OPTS="$JAVA_OPTS -Dspring.profiles.active=prod"

function check_if_process_is_running {
 if [ "$PID" = "" ]; then
 return 1
 fi
 ps -p $PID | grep "java"
 return $?
}


if check_if_process_is_running
then
	kill -9 $PID
fi

cd $GIT_DIR

git pull

rm -rf $WORK_DIR/code/*
git archive --format=tar.gz --prefix=$TAG-$DATE/ $TAG > $TAG_DIR/$DATE.tar.gz

rm -rf $WORK_DIR/source/*
tar zxvf $TAG_DIR/$DATE.tar.gz  -C $SOURCE_DIR/

cd $SOURCE_DIR/$TAG-$DATE

mvn -DskipTests=true install

cd ./target

sleep 3
#java -jar $JAVA_OPTS $PACKAGE_NAME
nohup java -jar $JAVA_OPTS $PACKAGE_NAME > log.out 2>&1&

echo "finished"