cd ..
cd content-service


docker login
docker tag hercules/content-service:1.0 herculeschrysanthos/content:1.0
docker push herculeschrysanthos/content:1.0