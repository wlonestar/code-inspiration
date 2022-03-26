# -*- coding=utf-8

import logging
import sys
import os
import time

from qcloud_cos import CosConfig, CosServiceError
from qcloud_cos import CosS3Client
from qcloud_cos.cos_threadpool import SimpleThreadPool

logging.basicConfig(level=logging.INFO, stream=sys.stdout)

secret_id = ''
secret_key = ''
region = ''
token = None
scheme = 'https'
test_bucket = ''
start_prefix = '/'
delimiter = ''

config = CosConfig(Region=region, SecretId=secret_id, SecretKey=secret_key, Token=token, Scheme=scheme)
client = CosS3Client(config)


def list_current_dir(prefix):
    file_info = []
    sub_dirs = []
    marker = ""
    count = 1
    while True:
        response = client.list_objects(test_bucket, prefix, delimiter, marker)
        # 调试输出
        # json_object = json.dumps(response, indent=4)
        # print(count, " =======================================")
        # print(json_object)
        count += 1

        if "CommonPrefixes" in response:
            common_prefixes = response.get("CommonPrefixes")
            sub_dirs.extend(common_prefixes)

        if "Contents" in response:
            contents = response.get("Contents")
            file_info.extend(contents)

        if "NextMarker" in response.keys():
            marker = response["NextMarker"]
        else:
            break

    print("=======================================================")

    # 如果 delimiter 设置为 "/"，则需要进行递归处理子目录，
    # sorted(sub_dirs, key=lambda sub_dir: sub_dir["Prefix"])
    # for sub_dir in sub_dirs:
    #     print(sub_dir)
    #     sub_dir_files = listCurrentDir(sub_dir["Prefix"])
    #     file_infos.extend(sub_dir_files)

    print("=======================================================")

    sorted(file_info, key=lambda file_info: file_info["Key"])
    for file in file_info:
        print(file)
    return file_info


# 下载文件到本地目录，如果本地目录已经有同名文件则会被覆盖；
# 如果目录结构不存在，则会创建和对象存储一样的目录结构
def download_files(file_info):
    local_dir = "./download/"

    pool = SimpleThreadPool()
    for file in file_info:
        # 文件下载 获取文件到本地
        file_cos_key = file["Key"]
        local_name = local_dir + file_cos_key
        # 如果本地目录结构不存在，递归创建
        if not os.path.exists(os.path.dirname(local_name)):
            os.makedirs(os.path.dirname(local_name))
        # skip dir, no need to download it
        if str(local_name).endswith("/"):
            continue
        # 实际下载文件
        # 使用线程池方式
        pool.add_task(client.download_file, test_bucket, file_cos_key, local_name)
        # 简单下载方式
        # response = client.get_object(
        #     Bucket=test_bucket,
        #     Key=file_cos_key,
        # )
        # response['Body'].get_stream_to_file(local_name)
    pool.wait_completion()
    return None


# 功能封装，下载对象存储上面的一个目录到本地磁盘
def download_dir_from_cos(prefix):
    global file_info

    try:
        file_info = list_current_dir(prefix)
    except CosServiceError as e:
        print(e.get_origin_msg())
        print(e.get_digest_msg())
        print(e.get_status_code())
        print(e.get_error_code())
        print(e.get_error_msg())
        print(e.get_resource_location())
        print(e.get_trace_id())
        print(e.get_request_id())
    download_files(file_info)
    return None


if __name__ == "__main__":
    begin = time.time()
    download_dir_from_cos(start_prefix)
    end = time.time()
    print(end - begin)
