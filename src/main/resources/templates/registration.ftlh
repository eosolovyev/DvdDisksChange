<#import "parts/common.ftlh" as c>
<@c.page>
    <form action="/registration" method="post">
        <div class="form-group">
            <label class="col-sm-2 col-form-label"> User Name : </label>
            <div class="col-sm-6">
                <input type="text" name="username" class="form-control" placeholder="User Name" />
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 col-form-label"> Password : </label>
            <div class="col-sm-6">
                <input type="password" name="password" class="form-control" placeholder="Password" />
            </div>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}" />

        <div class="form-group">
            <div class="col-sm-20">
                <button class="btn btn-primary" type="submit">Log In</button>
            </div>
        </div>
    </form>
</@c.page>