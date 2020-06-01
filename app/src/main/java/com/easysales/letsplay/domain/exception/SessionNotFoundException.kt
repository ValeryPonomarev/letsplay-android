package com.easysales.letsplay.data.exception

class SessionNotFoundException : ApiException("Session not found", CODES.SESSION_NOT_FOUND) {
}