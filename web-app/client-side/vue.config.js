module.exports = {

    configureWebpack: {
        devtool: 'source-map'
    },
    transpileDependencies: [
        "vuetify",
        "vue-sub",
        "signature_pad",
        "vue-signature-pad",
        "VueSignaturePad"
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
