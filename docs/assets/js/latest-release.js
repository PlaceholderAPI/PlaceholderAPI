document$.subscribe(async => {
    const api_code = document.querySelectorAll('[data-md-component="api-version"]');
    
    function loadAPIInfo(data) {
        const mcVersion = data["mcVersion"];
        const hyVersion = data["hyVersion"];

        const mcVersionToken = "{papiVersion}";
        const hyVersionToken = "{papiHytaleVersion}"
        for (const codeBlock of api_code) {
            codeBlock.innerHTML = codeBlock.innerHTML
                .replace(new RegExp(mcVersionToken, 'g'), mcVersion)
                .replace(new RegExp(hyVersionToken, 'g'), hyVersion);
        }
    }
    
    async function fetchAPIInfo() {
        const [mcRelease, hyRelease] = await Promise.all([
            fetch("https://repo.extendedclip.com/api/maven/latest/version/releases/me/clip/placeholderapi").then(_ => _.json()),
            fetch("https://repo.helpch.at/api/maven/latest/version/releases/at/helpch/placeholderapi-hytale").then(_ => _.json())
        ])
        
        const data = {
            "mcVersion": mcRelease.version,
            "hyVersion": hyRelease.version
        }
        
        __md_set("__api_tag", data, sessionStorage);
        loadAPIInfo(data);
    }
    
    if(location.href.includes("/developers/using-placeholderapi")) {
        const cachedApi = __md_get("__api_tag", sessionStorage);
        if ((cachedApi != null) && (cachedApi["mcVersion"])) {
            loadAPIInfo(cachedApi);
        } else {
            fetchAPIInfo();
        }
    }
})