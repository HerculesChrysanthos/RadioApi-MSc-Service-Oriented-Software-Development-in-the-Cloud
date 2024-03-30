INSTRUCTION TO RUN KUBERNTES CLUSTER

1)RUN MVN CLEAN\
2)RUN MVN INSTALL
3) target/kubernetes/kubernetes.yml REPLACE WITH IMAGE OF DOCKER HUB
4) target/kubernetes/kubernetes.json REPLACE WITH IMAGE OF DOCKER HUB
5) minikube start 
6) kubectl apply -f target/kubernetes/kubernetes.yml
7) kubectl get svc
7) minikube start service  to onoma pou tha ferei to kubectl get svc
8) prod.quarkus.hibernate-orm.sql-load-script=import.sql
9) done 



useful commands gia ton elegxo tou cluster

kubectl get pods--vlepoume thn katastash tou pod an einai running\
kubectl describe pod <pod-name>--detailed information about the pod and any events related to image pulling. This might provide additional insights into the issue.\
kubectl logs <service_name> ta logs tou cluster\
kubectl delete -f .  --diagrafh ta panta\
minikube kubectl -- expose deployment user-service --type=NodePort --port=8080--dhmiourgei nodeport 8080 gia to cluster useful for tuneling
