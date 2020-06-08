<#import "parts/common.ftlh" as c>
<@c.page>

    <i>Добавь новый диск</i>

    <form action="/addNewDisk" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label class="col-sm-2 col-form-label">Имя Диска</label>
            <div class="col-sm-6">
                <input type="text" name="name" class="form-control" placeholder="Disk Name" />
            </div>
        </div>


        <input type="hidden" name="_csrf" value="${_csrf.token}" />

        <div class="form-group">
            <div class="custom-file col-sm-6">
                <input type="file" name="file" placeholder="skin"/>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}" />

        <div class="form-group">
            <div class="col-sm-20">
                <button class="btn btn-primary" type="submit">Добавить</button>
            </div>
        </div>
    </form>
</@c.page>