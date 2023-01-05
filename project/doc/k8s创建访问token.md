# k8s创建访问token

> 如果需要使用k8s的java客户端，那么需要在集群中创建相应的访问token的资源。可按照如下步骤进行：

1、在指定的名称空间内创建一个ServiceAccount并创建相应的角色和绑定相应的角色

```yaml

# 创建一个ServiceAccount资源
apiVersion: v1
kind: ServiceAccount
metadata:
  name: java-client-service-account
  namespace: rootelement
  
---
# 创建相应的角色
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: java-client-cluster-role
  namespace: rootelement
rules:
  - apiGroups:
      - ""
      - apps
      - autoscaling
      - batch
      - extensions
      - policy
      - rbac.authorization.k8s.io
    resources:
      - nodes
      - endpoints
      - namespaces
      - deployments
      - replicasets
      - statefulsets
      - ingress
      - ingresses
      - services
      - pods
      - configmaps
      - persistentvolumeclaims
      - persistentvolumes
      - storageclasses
      - serviceaccounts
      - jobs
    verbs: ["get", "list", "watch", "create", "update", "patch", "delete"]

---
# 将service account绑定到角色
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: java-client-cluster-role-binding
subjects:
  - namespace: rootelement
    kind: ServiceAccount
    name: java-client-service-account
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: java-client-cluster-role

```

上面的YAML声明了一个**ClusterRole**和一个绑定到**java-client-service-account**的角色 <br/>
不建议在没有任何要求的情况下创建具有所有集群组件访问权限的服务帐户。要获取可用API资源的列表，请执行以下命令:

```bash

kubectl api-resources

```

2、验证是否ServiceAccount的访问权限

为了验证ClusterRole绑定，可以使用**can-i**命令来验证API访问。<br/>

例如，以下命令检查**rootelement**命名空间中的**java-client-service-account**是否可以列出pod：

```bash

kubectl auth can-i get pods --as=system:serviceaccount:rootelement:java-client-service-account

```

3、使用API调用验证服务帐户访问权限

首先要获取与service account关联的secret名称:

```bash

kubectl get serviceaccount java-client-service-account -o=jsonpath='{.secrets[0].name}' -n rootelement

```

使用secret名称解析出base64编码的token信息：

```bash

# kubectl get secrets  <service-account-token-name>  -o=jsonpath='{.data.token}' -n devops-tools | base64 -d


kubectl get secrets java-client-service-account-token-qp755 -o=jsonpath='{.data.token}' -n rootelement | base64 -d

```

获取集群端点以验证 API 访问。以下命令将显示集群端点（IP、DNS）:

```bash

kubectl get endpoints | grep kubernetes


```

使用curl和token信息访问接口：

```bash

curl -k https://192.168.8.131:6443/api/v1/namespaces -H "Authorization: Bearer <TOKEN>"

```
