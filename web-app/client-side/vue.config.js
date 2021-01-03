module.exports = {
    devServer: {
        host: '0.0.0.0',
        hot: true,
        disableHostCheck: true,
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
