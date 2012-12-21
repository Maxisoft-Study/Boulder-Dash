package boulderdash.cases;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.reflections.Reflections;

import boulderdash.annotations.UniqueInstance;

public class UniqueInstanceTest {

	@Test
	public void testMemeRefClone() {
		final Reflections reflections = new Reflections("boulderdash");
		Object obj = null;
		try {
			final Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(UniqueInstance.class);
			for (final Class<?> class1 : annotated) {
				@SuppressWarnings("unchecked")
				final Constructor<Object> constructor = (Constructor<Object>) class1.getDeclaredConstructor();
				constructor.setAccessible(true);
				obj = constructor.newInstance();
				final Method method = obj.getClass().getMethod("clone");
				final Object a = method.invoke(obj);
				Assert.assertTrue(a == obj);// meme ref
			}
		} catch (final NoSuchMethodException e) {
			Assert.fail("Un objets(" + obj.getClass().getCanonicalName()
					+ ") porte l'annotation @UniqueInstance et ne dispose pas de la methode clone\nDetail : " + e);
		} catch (final NoSuchMethodError e) {
			Assert.fail("Un objets(" + obj.getClass().getCanonicalName()
					+ ") porte l'annotation @UniqueInstance et ne dispose pas de la methode clone\nDetail : " + e);
		} catch (final AssertionError e) {
			Assert.fail("Un objets("
					+ obj.getClass().getCanonicalName()
					+ ") porte l'annotation @UniqueInstance et l'appel de la methode clone retourne une nouvelle instance.\nDetail : "
					+ e);
		} catch (final Exception e) {
			Assert.fail(e.toString());
		}

	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testNoPublicConstructor() {
		final Reflections reflections = new Reflections("boulderdash");
		final Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(UniqueInstance.class);
		for (final Class<?> class1 : annotated) {
			final Constructor[] allConstructors = class1.getDeclaredConstructors();
			for (final Constructor constructor : allConstructors) {

				Assert.assertFalse("Un objets(" + class1.getCanonicalName()
						+ ") porte l'annotation @UniqueInstance et Possede un constructeur public.",
						Modifier.isPublic(constructor.getModifiers()));

			}

		}

	}

}
