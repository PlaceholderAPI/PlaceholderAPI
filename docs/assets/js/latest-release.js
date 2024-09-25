document$.subscribe(async => {
    const api_code = document.querySelectorAll('[data-md-component="api-version"]');
    
    function loadAPIInfo(data) {
        const version = data["version"];
        const versionToken = "{version}";
        for (const codeBlock of api_code) {
            codeBlock.innerHTML = codeBlock.innerHTML.replace(new RegExp(versionToken, 'g'), version);
        }
    }
    
    async function fetchAPIInfo() {
        const release = await fetch("https://repo.extendedclip.com/api/maven/latest/version/releases/me/clip/placeholderapi").then(_ => _.json());
        
        console.log(release)
        
        const data = {
            "version": release.version
        }
        
        __md_set("__api_tag", data, sessionStorage);
        loadAPIInfo(data);
    }
    
    if(location.href.includes("/developers/using-placeholderapi")) {
        const cachedApi = __md_get("__api_tag", sessionStorage);
        if ((cachedApi != null) && (cachedApi["version"])) {
            loadAPIInfo(cachedApi);
        } else {
            fetchAPIInfo();
        }
    }
})