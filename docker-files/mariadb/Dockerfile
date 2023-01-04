FROM mariadb:10
ENV MYSQL_ROOT_PASSWORD test1234
ENV MYSQL_DATABASE test
COPY ./db/conf.d /etc/mysql/conf.d
COPY ./db/data /var/lib/mysql
COPY ./db/initdb.d /docker-entrypoint-initdb.d
EXPOSE 3306
ENTRYPOINT ["mysqld", "--user=root"]