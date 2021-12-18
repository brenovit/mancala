const authApi = {
    signin(userName) {
        return new Promise((resolve, reject) => {
            resolve({data : { name: userName, id: uuidv4()}})
        });
    }
}

export default authApi;