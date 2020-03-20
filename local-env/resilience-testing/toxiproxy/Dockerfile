FROM shopify/toxiproxy:2.1.4
COPY config.json config.json
COPY entry.sh entry.sh
RUN chmod +x entry.sh
ENTRYPOINT ["sh", "./entry.sh"]
