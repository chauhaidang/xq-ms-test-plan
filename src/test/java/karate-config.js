function fn() {
    const config = {
        baseUrl: 'http://localhost:8081',
    };

    const StringUtility = Java.type('com.xq.StringUtility');
    const utility = {
        randomStringPrefix: StringUtility.generateRandomStringPrefix,
        randomTitle: StringUtility.fnGenerateRandomStringPrefix(10, 'Title-' )
    }

    // Environment switching
    const env = karate.env || 'local';
    karate.log('karate.env:', env);

    return {...config, ...utility};
}