<#import "parts/common.ftlh" as c>
<@c.page>
    <div class="card" style="width: 25rem;">
        <#if disk.skinName??>
            <img src="/img/${disk.getSkinName()}" class="card-img-top" alt="...">
        </#if>
        <div class="card-body">
            <h5 class="card-title">Диск: ${disk.getName()}</h5>
            <#if isOwner>
               <i>Теперь вы владелец данного диска</i>
                <a href="/disk/${disk.id}/remove">Удалите этот диск если хотите</a>
             <#else>
                 <#if noOne>
                     <a href="/disk/${disk.id}/addFree">Добавьте этот диск себе, он бесплатый</a>
                  <#else>
                      <a href="/disk/${disk.id}/buy">Купите у пользователя ${disk.username}</a>
                 </#if>

            </#if>

        </div>
    </div>
</@c.page>