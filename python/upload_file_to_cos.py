# -*- coding=utf-8

import io
import os
import sys
import logging
import time

from PIL import Image, ImageFile

from qcloud_cos import CosConfig, CosS3Client

logging.basicConfig(level=logging.INFO, stream=sys.stdout)
secret_id = ''
secret_key = ''
region = ''
token = None
scheme = 'https'
bucket = ''
folder = ''

config = CosConfig(Region=region, Secret_id=secret_id, Secret_key=secret_key, Token=token, Scheme=scheme)
client = CosS3Client(config)


def get_size(file):
    size = os.path.getsize(file)
    return size / 1024


def get_outfile(infile, outfile):
    if outfile:
        return outfile
    dirt, suffix = os.path.splitext(infile)
    outfile = '{}{}'.format(dirt, suffix)
    return outfile


def compress_image(infile, outfile='', mb=1024, step=10, quality=80):
    o_size = get_size(infile)
    if o_size <= mb:
        return infile
    outfile = get_outfile(infile, outfile)
    while o_size > mb:
        im = Image.open(infile)
        im.save(outfile, quality=quality)
        if quality - step < 0:
            break
        quality -= step
        o_size = get_size(outfile)
    return outfile


if __name__ == '__main__':
    begin = time.time()
    file_name = 'dog.png'
    file = open(file_name, 'rb')
    response = client.put_object(
        Bucket=bucket,
        Body=file,
        Key=folder + '/' + file_name,
        StorageClass='STANDARD',
        EnableMD5=False
    )
    end = time.time()

    # print('https://' + bucket + '.cos.' + region + '.myqcloud.com/' + folder + '/' + os.path.basename(fp.name))
    print(end - begin)
