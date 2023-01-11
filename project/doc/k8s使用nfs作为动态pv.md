# 使用nfs作为动态PV存储方案

GitHub项目地址：[https://github.com/kubernetes-sigs/nfs-subdir-external-provisioner](https://github.com/kubernetes-sigs/nfs-subdir-external-provisioner)

1、创建认证相关资源和StorageClass

nfs-pv-auth.yml

```yml

# StorageClass设置
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: nfs-sc
  # 替换为自己需要部署的名称空间
  namespace: rootelement
provisioner: ljtnono/nfs-subdir-external-provisioner
parameters:
  archiveOnDelete: "false"
  # 配置动态生成的目录规则
  pathPattern: "${.PVC.namespace}/${.PVC.annotations.nfs.io/storage-path}"
---
# ServiceAccount设置
apiVersion: v1
kind: ServiceAccount
metadata:
  name: nfs-sa
  # 替换为自己需要部署的名称空间
  namespace: rootelement
---
# ClusterRole设置
kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: nfs-cr
rules:
  - apiGroups: [""]
    resources: ["nodes"]
    verbs: ["get", "list", "watch"]
  - apiGroups: [""]
    resources: ["persistentvolumes"]
    verbs: ["get", "list", "watch", "create", "delete"]
  - apiGroups: [""]
    resources: ["persistentvolumeclaims"]
    verbs: ["get", "list", "watch", "update"]
  - apiGroups: ["storage.k8s.io"]
    resources: ["storageclasses"]
    verbs: ["get", "list", "watch"]
  - apiGroups: [""]
    resources: ["events"]
    verbs: ["create", "update", "patch"]
---
# ClusterRoleBinding设置
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: nfs-crb
subjects:
  - kind: ServiceAccount
    name: nfs-sa
    # 替换为自己需要部署的名称空间
    namespace: rootelement
roleRef:
  kind: ClusterRole
  name: nfs-cr
  apiGroup: rbac.authorization.k8s.io
---
# Role设置
kind: Role
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: nfs-r
  # 替换为自己需要部署的名称空间
  namespace: rootelement
rules:
  - apiGroups: [""]
    resources: ["endpoints"]
    verbs: ["get", "list", "watch", "create", "update", "patch"]
---
# RoleBinding设置
kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: nfs-rb
  # 替换为自己需要部署的名称空间
  namespace: rootelement
subjects:
  - kind: ServiceAccount
    name: nfs-sa
      # 替换为自己需要部署的名称空间
    namespace: rootelement
roleRef:
  kind: Role
  name: nfs-r
  apiGroup: rbac.authorization.k8s.io


```

2、创建nfs-provisioner的deployment

nfs-pv-deployment.yml

```yml

# deployment配置
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nfs-provisioner-deployment
  labels:
    app: nfs-provisioner-deployment
  # 替换为自己需要部署的名称空间
  namespace: rootelement
spec:
  replicas: 1
  strategy:
    type: Recreate
  selector:
    matchLabels:
      app: nfs-provisioner
  template:
    metadata:
      labels:
        app: nfs-provisioner
    spec:
      serviceAccountName: nfs-sa
      containers:
        - name: nfs-provisioner
          image: ljtnono/nfs-subdir-external-provisioner:v4.0.2
          volumeMounts:
            - name: nfs-client-root
              # 挂载到容器内部的路径，不能更改
              mountPath: /persistentvolumes
          env:
            - name: PROVISIONER_NAME
              # 设置storageclass时的provisioner的值
              value: ljtnono/nfs-subdir-external-provisioner
              # NFS服务器的IP地址
            - name: NFS_SERVER
              value: 192.168.8.132
              # NFS服务器的路径
            - name: NFS_PATH
              value: /
      volumes:
        - name: nfs-client-root
          nfs:
            server: 192.168.8.132
            path: /home/nfs/dynamic

```

3、nfs动态pv服务高可用配置

nfs-pv-ha-deployment.yml

```yml

apiVersion: apps/v1
kind: Deployment
metadata:
  name: nfs-provisioner-deployment
  labels:
    app: nfs-provisioner-deployment
  # 替换为自己需要部署的名称空间
  namespace: rootelement
spec:
  # 因为要实现高可用，所以配置3个pod副本
  replicas: 3
  strategy:
    type: Recreate
  selector:
    matchLabels:
      app: nfs-provisioner
  template:
    metadata:
      labels:
        app: nfs-provisioner
    spec:
      serviceAccountName: nfs-sa
      imagePullSecrets:
        - name: registry-auth-paas
      containers:
        - name: nfs-provisioner
          image: ljtnono/nfs-subdir-external-provisioner:v4.0.2
          imagePullPolicy: IfNotPresent
          volumeMounts:
            - name: nfs-client-root
              mountPath: /persistentvolumes
          env:
            - name: PROVISIONER_NAME
              value: ljtnono/nfs-subdir-external-provisioner
              # 设置高可用允许选举
            - name: ENABLE_LEADER_ELECTION
              value: "True"
            - name: NFS_SERVER
              value: 192.168.8.132
            - name: NFS_PATH
              value: /
      volumes:
        - name: nfs-client-root
          nfs:
            server: 192.168.8.132
            path: /home/nfs/dynamic

```

4、创建PVC时自定义子目录，从以上配置文件可以看出，使用annotation标签可以自定义PV的存储路径，例如：

```yml

kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: test
  namespace: rootelement
  annotations:
    # 子目录标签
    nfs.io/storage-path: test
spec:
  storageClassName: nfs-sc
  accessModes:
    - ReadWriteMany
  resources:
    requests:
      storage: 10Gi

```
