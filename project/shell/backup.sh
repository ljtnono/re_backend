#!/bin/bash

# 文件保存路径
SAVE_FOLDER_PATH=/Users/lingjiatong/document/文件备份/re/mysql
# mysqldump执行路径
MYSQLDUMP_EXE_PATH=/Users/lingjiatong/software/mysql8/bin/mysqldump
# mysql连接配置
HOST=www.lingjiatong.cn
PORT=30006
USER=root
PASSWORD=re#mysql2022
# 需要导出的数据库名称列表
DATABASES=('nacos' 're_article' 're_sys' 'xxl_job')

backup() {
  # 获取当前日期
  DATE=`date +'%Y%m%d'`
  # 创建文件夹
  if [ -e $SAVE_FOLDER_PATH/$DATE ]; then
    rm -rf $SAVE_FOLDER_PATH/$DATE
  fi
  # 只包含结构
  mkdir -p $SAVE_FOLDER_PATH/$DATE/struct
  # 包含数据和结构
  mkdir -p $SAVE_FOLDER_PATH/$DATE/data
  # 备份数据库
  for database in ${DATABASES[@]}; do
    $MYSQLDUMP_EXE_PATH -h $HOST -P $PORT -u$USER -p$PASSWORD -d $database > $SAVE_FOLDER_PATH/$DATE/struct/$database.sql
    $MYSQLDUMP_EXE_PATH -h $HOST -P $PORT -u$USER -p$PASSWORD $database > $SAVE_FOLDER_PATH/$DATE/data/$database.sql
  done
  # 打包成为压缩包
  cd $SAVE_FOLDER_PATH/$DATE
  tar czvf struct.tar.gz struct/*.sql
  tar czvf data.tar.gz data/*.sql
  rm -rf struct
  rm -rf data
}

backup

exit 0