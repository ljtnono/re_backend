#!/usr/local/Cellar/python@3.9/3.9.6/bin/python3

import os
from pymysql import connect
from datetime import datetime
import json
# mysql连接信息
MYSQL_CONFIG = {
    "host": "localhost",
    "port": 3306,
    "user": "root",
    "password": "ljtLJT715336",
    "database": "re"
}
# markdown文件所在文件夹
MARKDOWN_FOLDER = "/Users/lingjiatong/document/markdown/study_note"
# 默认图片访问url
DEFAULT_PIC_URL = "https://192.168.137.12/minio/images/default.png"

if __name__ == "__main__":
    connect = connect(**MYSQL_CONFIG)
    cursor = connect.cursor()
    for dirpath, dirnames, filenames in os.walk(MARKDOWN_FOLDER):
        for filename in filenames:
            # 获取其中以md为后缀名的文件
            if str.endswith(filename, ".md"):
                with open(os.path.join(dirpath, filename), mode="r", encoding="utf8") as file:
                    current_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                    create_time = current_time
                    modify_time = current_time
                    markdown_content = str(file.read())
                    if len(markdown_content) == 0:
                        markdown_content = ''
                    value = (filename[0:filename.find(".md")], "暂无简介内容", markdown_content, create_time, modify_time, 0, 1, 1, DEFAULT_PIC_URL, 0, 0, 1, 1) 
                    insert_sql = 'INSERT INTO tb_blog_article(title, summary, markdown_content, create_time, modify_time, is_deleted, type_id, user_id, cover_url, view, favorite, is_draft, is_recommend)' \
                        'VALUES(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)'
                    cursor.execute(insert_sql, value)
                    connect.commit()