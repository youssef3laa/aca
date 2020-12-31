module.exports = {
    devServer: {
        disableHostCheck: true
    },
    configureWebpack: {
        devtool: 'source-map'
    },
    transpileDependencies: [
        "vuetify",
        "vue-sub"
    ],

    pluginOptions: {
        i18n: {
            locale: 'en',
            fallbackLocale: 'en',
            localeDir: 'locales',
            enableInSFC: true
        }
    }
}
