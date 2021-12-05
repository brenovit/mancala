const options = {
    moduleCache: { vue: Vue },
    async getFile(url) {
        console.log(url);
        const res = await fetch(url);
        if ( !res.ok ) {
        throw Object.assign(new Error(res.statusText + ' ' + url), { res });
        }
        return {
        getContentData: (asBinary) => asBinary ? res.arrayBuffer() : res.text(),
        }
    },
    addStyle: () => {},
};

const options2 = {
    moduleCache: { vue: Vue },
    getFile(url) {
        console.log(url);
        return fetch(url).then(response => response.ok ? response.text() : Promise.reject(response));
    },
    addStyle: () => {}
};

const { loadModule } = window['vue3-sfc-loader'];