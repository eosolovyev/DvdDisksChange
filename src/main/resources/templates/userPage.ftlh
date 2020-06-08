<#import "parts/common.ftlh" as c>
<@c.page>
    <i>Пользователь: ${user.username}</i>
    <div class="container">
        <div class="row">
            <div class="col-sm">
                Текущие диски пользователя
                <#if isCurrentDisks>
                    <#list currentDisks as d>
                        <div class="card" style="width: 15rem;">
                            <#if d.skinName??>
                                <img src="/img/${d.getSkinName()}" class="card-img-top" alt="...">
                            </#if>
                            <div class="card-body">
                                <h5 class="card-title">Диск: ${d.getName()}</h5>
                                <#if isCurrentUser>
                                    <a href="/disk/${d.id}/remove">Удалите этот диск если хотите</a>
                                <#else>
                                    <a href="/disk/${d.id}/buy">Купите у пользователя ${d.username}</a>
                                </#if>
                            </div>
                        </div>
                    </#list>
                <#else>
                    а дисков нет :/
                </#if>
            </div>
            <div class="col-sm">
                Прошлые диски пользователя
                <#if isOldDisks>
                    <#list oldDisks as d>
                        <div class="card" style="width: 15rem;">
                            <#if d.skinName??>
                                <img src="/img/${d.getSkinName()}" class="card-img-top" alt="...">
                            </#if>
                            <div class="card-body">
                                <h5 class="card-title">Диск: ${d.getName()}</h5>
                                <#if d.isFree()>
                                    Данный диск сейчас никому не принадлежит
                                    <a href="/disk/${d.id}/addFree">Взять свободный диск</a>
                                <#else>
                                    текущий владелец ${d.username}
                                    <a href="/disk/${d.id}/buy">Купить у пользователя</a>
                                </#if>
                            </div>
                        </div>
                    </#list>
                <#else>
                    а дисков нет :/
                </#if>
            </div>
            </div>
        </div>
    </div>
</@c.page>