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
        const release = await fetch("https://papi-repo-proxy.vercel.app/repo/latest-release").then(_ => _.json());
        
        const data = {
            "version": release.name
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