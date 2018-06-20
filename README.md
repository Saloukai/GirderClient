# GirderClient
[JAVA] Interface to communicate with Girder

## External Librairies
* [okHTTP](https://square.github.io/okhttp/)
* [okIO](https://github.com/square/okio)
* [Json-Simple](https://code.google.com/archive/p/json-simple/downloads)

## Use
```java
GirderClient girderClient = new GirderClient("GirderApiKey");

// Download a file from the id of a girder file
girderClient.downloadFile(fileID);
```

![CREATIS](https://www.creatis.insa-lyon.fr/site7/sites/www.creatis.insa-lyon.fr/files/logo-creatis.png)