FOR PUSH IMAGE  TO DOCKER HUB\
RUN DOCKER_HUB_UPLOAD.SH\
REPLACE sifavs/user:1.0 WITH YOUR IMAGE\
REPLACE ksif/user:1.0   WITH your namespace


to download image from DOCKER HUB\
RUN DOCKER_HUB_DOWNLOAD.SH\
REPLACE ksif WITH your namespace

