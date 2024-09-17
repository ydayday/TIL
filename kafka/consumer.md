# 컨슈머(Consumer)

- 동작하던 컨슈머를 종료하고 재기동할 경우 이전에 읽었던 offset부터 데이터를 읽어온다.
- 
## 💡 오프셋을 찾아 조인 후 데이터를 읽는다.

```
[main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-my-java-application-1, groupId=my-java-application] Setting offset for partition demo_java-2 to the committed offset FetchPosition{offset=294, offsetEpoch=Optional[0], currentLeader=LeaderAndEpoch{leader=Optional[localhost:9092 (id: 1 rack: null)], epoch=0}
```


## 💡 auto.offset.reset 설정

- 세가지 옵션
  : **none** : 컨슈머그룹이 없을 경우 동작하지 않는다. (동작 전에 컨슈머 그룹을 만들어야 함)
  : **earliest** : 토픽을 처음부터 읽는다.
  : **latest** : 새 메세지만 읽는다.


## 💡 컨슈머 그룹과 파티션 리밸런싱
  - 컨슈머가 그룹에 합류하거나 나갈 때마다 파티션이 이동하는데 이를 리밸런싱이라 한다.
  : **Eager Rebalance** : 새로운 컨슈머가 추가될 경우 모든 컨슈머의 읽기 작업이 중단되며, 모든 컨슈머가 이전에 속했던 컨슈머 그룹에 다시 합류해서 새 파티션을 할당받는다. <br>
  - 기존 있던 파티션으로 되돌아간다는 보장이 없다.
  : **Cooperative Rebalance** : 모든 파티션을 모든 컨슈머에게 재할당하는 게 아니라 파티션을 작은 그룹을 나누어 일부만 한 컨슈머에서 다른 컨슈머로 재할당한다.

## 💡 Static Group Membership - 정적 그룹 멤버쉽
 - 컨슈머를 재시작하려고 할 때 리벨런싱이 일어날 걱정을 하지 않아도 된다.
 - 컨슈머에 group.instance.id를 부여하게되면 정적인 컨슈머를 만들 수 있다.
 - 정적 컨슈머가 나가면 설정한 세션 시간 내에 다시 합류하면 재할당을 하지 않고 기존 파티션을 사용할 수 있다.
 - 쿠버네티스에 유용하다.

  
