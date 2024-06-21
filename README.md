# Multitenant OAUTH2 Authorization server built with Java-Spring.

openssl genrsa -out authkit_private_key.pem 4096 && \
openssl rsa -pubout -in authkit_private_key.pem -out authkit_public_key.pem && \
openssl pkcs8 -topk8 -in authkit_private_key.pem -inform pem -out authkit_private_key_pkcs8.pem -outform pem -nocrypt
