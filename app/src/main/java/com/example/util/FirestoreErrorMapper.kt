package com.example.util

import androidx.annotation.StringRes
import com.example.R
import com.google.firebase.firestore.FirebaseFirestoreException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

enum class NetworkErrorKind {
    OFFLINE,
    SESSION_OR_PERMISSION,
    SERVER_OR_OTHER
}

data class MappedErrorUi(
    val kind: NetworkErrorKind,
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int,
    @StringRes val actionLabelRes: Int
)

fun classifyFirestoreError(error: Throwable?): NetworkErrorKind {
    var current: Throwable? = error
    while (current != null) {
        when (current) {
            is FirebaseFirestoreException -> {
                return when (current.code) {
                    FirebaseFirestoreException.Code.UNAUTHENTICATED,
                    FirebaseFirestoreException.Code.PERMISSION_DENIED -> NetworkErrorKind.SESSION_OR_PERMISSION
                    FirebaseFirestoreException.Code.UNAVAILABLE,
                    FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> NetworkErrorKind.OFFLINE
                    else -> NetworkErrorKind.SERVER_OR_OTHER
                }
            }
            is UnknownHostException,
            is ConnectException,
            is SocketTimeoutException -> return NetworkErrorKind.OFFLINE
        }
        current = current.cause
    }
    val msg = error?.message.orEmpty().lowercase()
    return when {
        "permission" in msg || "unauthenticated" in msg || "auth" in msg -> NetworkErrorKind.SESSION_OR_PERMISSION
        "offline" in msg || "unavailable" in msg || "network" in msg || "timeout" in msg -> NetworkErrorKind.OFFLINE
        else -> NetworkErrorKind.SERVER_OR_OTHER
    }
}

fun mapErrorKindToUi(kind: NetworkErrorKind): MappedErrorUi {
    return when (kind) {
        NetworkErrorKind.OFFLINE -> MappedErrorUi(
            kind = kind,
            titleRes = R.string.forum_disconnected_title,
            messageRes = R.string.forum_load_error,
            actionLabelRes = R.string.forum_retry_btn
        )
        NetworkErrorKind.SESSION_OR_PERMISSION -> MappedErrorUi(
            kind = kind,
            titleRes = R.string.error_session_title,
            messageRes = R.string.error_session_desc,
            actionLabelRes = R.string.error_session_retry
        )
        NetworkErrorKind.SERVER_OR_OTHER -> MappedErrorUi(
            kind = kind,
            titleRes = R.string.error_server_title,
            messageRes = R.string.error_server_desc,
            actionLabelRes = R.string.forum_retry_btn
        )
    }
}
