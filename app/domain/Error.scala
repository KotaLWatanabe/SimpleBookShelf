package domain

trait AppError

sealed trait DomainError extends AppError
case class IllegalArgumentDomainError(e: Throwable, clazz: Class[_]) extends DomainError
