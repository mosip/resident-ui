FROM nginx

# can be passed during Docker build as build time environment for github branch to pickup configuration from.
#ARG preregistration_i18n_bundle_url_arg=http://artifactory-service/artifactory/libs-release-local/i18n/resident-i18n-bundle.zip

# can be passed during Docker build as build time environment for github branch to pickup configuration from.
ARG resident_i18n_bundle_url_arg

# can be passed during Docker build as build time environment variable for mosip user level change.
ARG container_user=mosip
ARG container_user_group=mosip
ARG container_user_uid=1001
ARG container_user_gid=1001

# can be passed during Docker build as build time environment for label related addition to docker.
ARG SOURCE
ARG COMMIT_HASH
ARG COMMIT_ID
ARG BUILD_TIME

ENV base_path=/usr/share/nginx/html

ENV i18n_path=${base_path}/assets/i18n

# environment variable to pass artifactory url, at docker runtime
ENV resident_i18n_bundle_url_env=${resident_i18n_bundle_url_arg}

# can be passed during Docker build as build time environment for label.
LABEL source=${SOURCE}
LABEL commit_hash=${COMMIT_HASH}
LABEL commit_id=${COMMIT_ID}
LABEL build_time=${BUILD_TIME}
   
# set working directory for the user
WORKDIR /home/${container_user}

ADD ./nginx.conf /etc/nginx/nginx.conf

ADD default.conf /etc/nginx/conf.d/

ADD dist ${base_path}

## install packages, create user and change permissions of file inside working dir
RUN apt-get -y update \
&& apt-get install -y unzip wget \
&& groupadd -g ${container_user_gid} ${container_user_group} \
&& useradd -u ${container_user_uid} -g ${container_user_group} -s /bin/sh -m ${container_user} \
&& mkdir -p /var/run/nginx /var/tmp/nginx \
&& chown -R ${container_user}:${container_user} /home/${container_user} /usr/share/nginx /var/run/nginx /var/tmp/nginx ${base_path}/assets/i18n 
 
# select container user for all tasks
USER ${container_user_uid}:${container_user_gid}

EXPOSE 8080

CMD wget -q --show-progress "${resident_i18n_bundle_url_env}" -O "${i18n_path}"/resident-i18n-bundle.zip; \
   cd ${i18n_path} ; \
   unzip -o resident-i18n-bundle.zip ; \
   nginx ; \
   sleep infinity
