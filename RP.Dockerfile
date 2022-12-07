FROM php:7.4-apache


COPY rp-conf/ /etc/apache2


RUN a2enmod proxy proxy_http proxy_balancer lbmethod_byrequests status
RUN a2ensite 000-* 001-*

