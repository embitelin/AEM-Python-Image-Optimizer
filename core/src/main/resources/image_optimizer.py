import PIL
import sys
import requests
import io
import os

from PIL import Image
from tkinter.filedialog import *
def read_arguments():
    arguments = sys.argv[1:]  # Exclude the script name itself
    return arguments

def extract_file_info(string):
    last_slash_index = string.rfind('/')
    file_path = string[:last_slash_index + 1] if last_slash_index != -1 else ""
    file_name = string[last_slash_index + 1:]
    return file_path, file_name

def get_image_name_and_extension(image_name_with_extension):
    image_name, image_extension = os.path.splitext(image_name_with_extension)
    return image_name, image_extension

def get_jpeg_image(jpeg_image_extension):
      exten = jpeg_image_extension[1:]
      if exten.find("jpg") != -1 :
            exten = exten.replace("jpg", "jpeg").upper()
      else:
            exten = exten.upper()
      return exten

def call_post_api(file_path, api_url, userName, passwrd):
    auth = (userName, passwrd)
    with open(file_path, 'rb') as file:
        image_data = file.read()
    headers = {'Content-Type': 'image/jpeg'}
    response = requests.post(api_url, auth=auth, data=image_data, headers=headers)
    return response

def invoke_get_api(api_url, userName, passwrd):
    auth = (userName, passwrd)
    response = requests.get(api_url, auth=auth)
    return response

def convert_string_to_url(string):
    encoded_url = string.replace(' ', '%20')
    return encoded_url

def write_response_to_file(response, file_path):
    with open(file_path, 'wb') as file:
        file.write(response.content)

arguments = read_arguments()
filename = "image_path_from_java"
instanceUrl = "instanceurl"
uname = "username"
pwd = "password"
print("Python File Name :",filename )
if filename.find("jcr:content/renditions/") == -1:
    print("File Name :",filename )
    file_path, file_name = extract_file_info(filename)
    print("File Path:", file_path)
    print("File Name:", file_name)
    api_url = instanceUrl+filename

    response = invoke_get_api(api_url, uname, pwd)
    imagename, imageext = get_image_name_and_extension(file_name)
    image_path_res = imagename +'_compressed' + imageext
    compressed_image =image_path_res
    write_response_to_file(response, image_path_res)

    # Handle the response
    print("Response status code:", response.status_code)
    img = Image.open(image_path_res)
    img.save(compressed_image, get_jpeg_image(imageext), optimize = True, quality = 50)

    api_url = instanceUrl + '/api/assets'+file_path+compressed_image
    api_url = api_url.replace("/content/dam/", "/")
    print("API URL:", api_url)

    response = call_post_api(image_path_res, api_url, uname, pwd)
    os.remove(compressed_image)

    # Handle the response
    print("Response status code:", response.status_code)
else:
    print("Substring found!")