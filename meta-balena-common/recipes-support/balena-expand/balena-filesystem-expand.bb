DESCRIPTION = "Balena data partition filesystem expander"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${BALENA_COREBASE}/COPYING.Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = " \
    file://balena-filesystem-expand \
    file://balena-filesystem-expand.service \
    "
S = "${WORKDIR}"

inherit allarch systemd

SYSTEMD_SERVICE_${PN} = "balena-filesystem-expand.service"

RDEPENDS_${PN} = " \
    coreutils \
    e2fsprogs-resize2fs \
    util-linux \
    "

do_install() {
    install -d ${D}${bindir}
    install -m 0775 ${WORKDIR}/balena-filesystem-expand ${D}${bindir}

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}${systemd_unitdir}/system
        install -c -m 0644 ${WORKDIR}/balena-filesystem-expand.service ${D}${systemd_unitdir}/system
        sed -i -e 's,@BASE_BINDIR@,${base_bindir},g' \
            -e 's,@SBINDIR@,${sbindir},g' \
            -e 's,@BINDIR@,${bindir},g' \
            ${D}${systemd_unitdir}/system/*.service
    fi
}
