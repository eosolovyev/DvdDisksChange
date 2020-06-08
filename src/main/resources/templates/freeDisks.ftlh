<#import "parts/common.ftlh" as c>
<@c.page>
    <#if isFreeDisks>
        <#list disks as d>
            <div class="card" style="width: 18rem;">
                <#if d.skinName??>
                    <img src="/img/${d.getSkinName()}" class="card-img-top" alt="...">
                </#if>
                <div class="card-body">
                    <h5 class="card-title">Диск: ${d.getName()}</h5>
                    <i><a href="/disk/${d.getId()}/addFree">Получить бесплатный диск</a></i>
                </div>
            </div>
        </#list>
    <#else>
        <i>Список свободных дисков пуст</i>
    </#if>
</@c.page>